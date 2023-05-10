#include "adc.h"
#include "delay.h"

#include <stm32f10x_conf.h>


void ADCX_UserConfig(void) {
	
	GPIO_InitTypeDef GPIO_InitStructure;
	ADC_InitTypeDef ADCX_InitStructure;
	
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOA | RCC_APB2Periph_AFIO | RCC_APB2Periph_ADC1,ENABLE);
	RCC_ADCCLKConfig(RCC_PCLK2_Div6);  //72mHZ/6 = 12m < 14M
	
	GPIO_InitStructure.GPIO_Pin  = AD0;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AIN;  //模拟量输入模式
	GPIO_Init(ADCX_PROT,&GPIO_InitStructure);
	
	ADCX_InitStructure.ADC_Mode = ADC_Mode_Independent;
	ADCX_InitStructure.ADC_ContinuousConvMode = ENABLE;
	ADCX_InitStructure.ADC_ScanConvMode = DISABLE;  //单通道
	ADCX_InitStructure.ADC_ExternalTrigConv = ADC_ExternalTrigConv_None;
	ADCX_InitStructure.ADC_DataAlign = ADC_DataAlign_Right;
	ADCX_InitStructure.ADC_NbrOfChannel = 1;
	ADC_Init(ADC1,&ADCX_InitStructure);
	
	ADC_RegularChannelConfig(ADC1,ADC_Channel_0,1,ADC_SampleTime_239Cycles5);
	
	ADC_Cmd(ADC1,ENABLE);
	
	ADC_ResetCalibration(ADC1);  //重置指定adc的校准寄存器
	while(ADC_GetResetCalibrationStatus(ADC1));  //获取adc重置校准寄存器的的状态
	
	ADC_StartCalibration(ADC1);  //开始指定adc校准
	while(ADC_GetCalibrationStatus(ADC1));  //获取指定adc的校准状态
	
	ADC_SoftwareStartConvCmd(ADC1,ENABLE);  //开启软件转换
}

u16 Get_ADC(void) {

	u32 value = 0;
	u8 i = 0;
	for(i=0; i<10; i++){
	
		value += ADC_GetConversionValue(ADC1);
		while(ADC_GetFlagStatus(ADC1,ADC_FLAG_EOC) == RESET);
	}
	value /= 10;
	
	delay_ms(100);
	
	return value;  // 获取电压值  value*3.3/4096
}



