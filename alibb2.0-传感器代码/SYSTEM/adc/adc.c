#include "adc.h"
#include "delay.h"

#include <stm32f10x_conf.h>


void ADCX_UserConfig(void) {
	
	GPIO_InitTypeDef GPIO_InitStructure;
	ADC_InitTypeDef ADCX_InitStructure;
	
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOA | RCC_APB2Periph_AFIO | RCC_APB2Periph_ADC1,ENABLE);
	RCC_ADCCLKConfig(RCC_PCLK2_Div6);  //72mHZ/6 = 12m < 14M
	
	GPIO_InitStructure.GPIO_Pin  = AD0;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AIN;  //ģ��������ģʽ
	GPIO_Init(ADCX_PROT,&GPIO_InitStructure);
	
	ADCX_InitStructure.ADC_Mode = ADC_Mode_Independent;
	ADCX_InitStructure.ADC_ContinuousConvMode = ENABLE;
	ADCX_InitStructure.ADC_ScanConvMode = DISABLE;  //��ͨ��
	ADCX_InitStructure.ADC_ExternalTrigConv = ADC_ExternalTrigConv_None;
	ADCX_InitStructure.ADC_DataAlign = ADC_DataAlign_Right;
	ADCX_InitStructure.ADC_NbrOfChannel = 1;
	ADC_Init(ADC1,&ADCX_InitStructure);
	
	ADC_RegularChannelConfig(ADC1,ADC_Channel_0,1,ADC_SampleTime_239Cycles5);
	
	ADC_Cmd(ADC1,ENABLE);
	
	ADC_ResetCalibration(ADC1);  //����ָ��adc��У׼�Ĵ���
	while(ADC_GetResetCalibrationStatus(ADC1));  //��ȡadc����У׼�Ĵ����ĵ�״̬
	
	ADC_StartCalibration(ADC1);  //��ʼָ��adcУ׼
	while(ADC_GetCalibrationStatus(ADC1));  //��ȡָ��adc��У׼״̬
	
	ADC_SoftwareStartConvCmd(ADC1,ENABLE);  //�������ת��
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
	
	return value;  // ��ȡ��ѹֵ  value*3.3/4096
}



