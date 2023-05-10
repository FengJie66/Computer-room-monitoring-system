// C��
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


u8 humidity;    //ʪ����������
u8 temperature;  //�¶���������
u16 smoke;   //�̸�

char PUB_BUF[256];  //�ϴ����ݵ�buf
const char *devSubTopic[] = {"/hgfsmarthome/sub"};
const char devPubTopic[] = "/hgfsmarthome/pub";

 int main(void)
 {
	 
	 
	unsigned short timeCount = 0;	//���ͼ������
	
	unsigned char *dataPtr = NULL;
	 
	delay_init();    	 //��ʱ������ʼ��	  
	LED_Init();		  	//��ʼ����LED���ӵ�Ӳ���ӿ�
	 
	DHT11_Init();  // ��ʼ��dht11
	 
	ADCX_UserConfig();  //adc��ʼ��
	 
	Usart1_Init(115200);  //����1��ʼ��Ϊ115200
	Usart2_Init(115200);  //����2��ʼ��Ϊ115200 8266ͨѶ����
	UsartPrintf(USART_DEBUG, " Hardware init OK\r\n");
	 
	ESP8266_Init();					//��ʼ��ESP8266
	 
	while(OneNet_DevLink())			//����OneNET
		delay_ms(500);
	
	 OneNet_Subscribe(devSubTopic, 1);
	 
	while(1)
	{
		
		GPIO_ResetBits(GPIOA,GPIO_Pin_4); //LED0�����
		GPIO_SetBits(GPIOC,GPIO_Pin_13);//LED1�����
		delay_ms(300);
		GPIO_SetBits(GPIOA,GPIO_Pin_4);//LED0�����
		GPIO_ResetBits(GPIOC,GPIO_Pin_13);//LED1�����
		delay_ms(300);
		
		// ��ʪ�Ȼ�ȡ����
		
		
		DHT11_Read_Data(&temperature,&humidity); //��ȡ��ʪ��ֵ
		smoke = Get_ADC();   //��ȡ�̸�ֵ
		
		UsartPrintf(USART1,"deviceId��68f4b8a1-cd16-2f1c-6b6f-5c7f01f3112b\n �¶ȣ�%d ʪ�ȣ�%d �̸У�%d\r\n",temperature,humidity,smoke);
		
		if(timeCount % 40 == 0) {
			// ��ʪ�Ȼ�ȡ����
			DHT11_Read_Data(&temperature,&humidity); //��ȡ��ʪ��ֵ �̸�
			UsartPrintf(USART1,"deviceId��68f4b8a1-cd16-2f1c-6b6f-5c7f01f3112b\n �¶ȣ�%d ʪ�ȣ�%d �̸У�%d\r\n",temperature,humidity,smoke);
		}
		
		
		if(++timeCount >= 20)									//���ͼ��5s
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

