// C库
#include <string.h>
#include "stdio.h"

#include "led.h"
#include "delay.h"
#include "sys.h"
#include "dht11.h"
#include "usart.h"
#include "esp8266.h"
#include "onenet.h"
#include "adc.h"


u8 humidity;    //湿度整数部分
u8 temperature;  //温度整数部分
u16 smoke;   //烟感

char PUB_BUF[256];  //上传数据的buf
const char *devSubTopic[] = {"/hgfsmarthome/sub"};
const char devPubTopic[] = "/hgfsmarthome/pub";

 int main(void)
 {
	 
	 
	unsigned short timeCount = 0;	//发送间隔变量
	
	unsigned char *dataPtr = NULL;
	 
	delay_init();    	 //延时函数初始化	  
	LED_Init();		  	//初始化与LED连接的硬件接口
	 
	DHT11_Init();  // 初始化dht11
	 
	ADCX_UserConfig();  //adc初始化
	 
	Usart1_Init(115200);  //串口1初始化为115200
	Usart2_Init(115200);  //串口2初始化为115200 8266通讯串口
	UsartPrintf(USART_DEBUG, " Hardware init OK\r\n");
	 
	ESP8266_Init();					//初始化ESP8266
	 
	while(OneNet_DevLink())			//接入OneNET
		delay_ms(500);
	
	 OneNet_Subscribe(devSubTopic, 1);
	 
	while(1)
	{
		
		GPIO_ResetBits(GPIOA,GPIO_Pin_4); //LED0输出低
		GPIO_SetBits(GPIOC,GPIO_Pin_13);//LED1输出高
		delay_ms(300);
		GPIO_SetBits(GPIOA,GPIO_Pin_4);//LED0输出高
		GPIO_ResetBits(GPIOC,GPIO_Pin_13);//LED1输出低
		delay_ms(300);
		
		// 温湿度获取数据
		
		
		DHT11_Read_Data(&temperature,&humidity); //读取温湿度值
		smoke = Get_ADC();   //读取烟感值
		
		UsartPrintf(USART1,"deviceId：68f4b8a1-cd16-2f1c-6b6f-5c7f01f3112b\n 温度：%d 湿度：%d 烟感：%d\r\n",temperature,humidity,smoke);
		
		if(timeCount % 40 == 0) {
			// 温湿度获取数据
			DHT11_Read_Data(&temperature,&humidity); //读取温湿度值 烟感
			UsartPrintf(USART1,"deviceId：68f4b8a1-cd16-2f1c-6b6f-5c7f01f3112b\n 温度：%d 湿度：%d 烟感：%d\r\n",temperature,humidity,smoke);
		}
		
		
		if(++timeCount >= 20)									//发送间隔5s
		{
			UsartPrintf(USART_DEBUG, "OneNet_Publish\r\n");
			sprintf(PUB_BUF,"{\"deviceId\":68f4b8a1-cd16-2f1c-6b6f-5c7f01f3112b,\"tem\":%d,\"hum\":%d,\"smoke\":%d}",temperature,humidity,smoke);
			OneNet_Publish(devPubTopic, PUB_BUF);
			
			timeCount = 0;
			ESP8266_Clear();
		}
		
		dataPtr = ESP8266_GetIPD(3);
		if(dataPtr != NULL)
			OneNet_RevPro(dataPtr);
		
		delay_ms(10);
	}
 }

