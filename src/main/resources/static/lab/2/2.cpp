#include<iostream>
#include<inttypes.h>
#include<stdio.h>
#include<stdint.h>
using namespace std;
const uint32_t MOD_ADLER=65521;

uint32_t adler32(unsigned char *data, size_t len){
	uint32_t csum1=1;
	uint32_t csum2=0;
	size_t i;
	for(i=0;i<len;i++){
		csum1=(csum1+data[i])%MOD_ADLER;
		csum2=(csum2+csum1)%MOD_ADLER;
	}
	return (csum2<<16)|csum1;
}

uint16_t fletcher16(uint8_t *data, int count){
	uint16_t csum1=0;
	uint16_t csum2=0;
	int i;
	for(i=0;i<count;i++){
		csum1=(csum1+data[i])%255;
		csum2=(csum2+csum1)%255;
	}
	return (csum2<<8)|csum1;
}

int main(){
	uint8_t data[40];
	cout<<"Enter the data: ";
	cin>>data;
	uint16_t fletcher;
	uint32_t adler;
	fletcher=fletcher16(data,16);
	adler=adler32(data,32);
	cout<<"Adler checksum for "<<data<<" is "<<adler<<endl;
	cout<<"Fletcher checksum for "<<data<<" is "<<fletcher<<endl;
}
