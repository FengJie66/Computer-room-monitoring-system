#ifndef __ADC_H
#define __ADC_H	
#include <stm32f10x_conf.h>


#define AD0 GPIO_Pin_0
#define ADCX_PROT GPIOA

void ADCX_UserConfig(void);
u16 Get_ADC(void);

#endif 

