#include<sys/types.h>
#include<netinet/in.h>
#include<sys/stat.h>
#include<stdlib.h>
#include<fcntl.h>
#include<stdio.h>
int main(){
	int c_socket,s=1024;
	char *buf=malloc(s);
	char fname[256];
	struct sockaddr_in address;
	c_socket=socket(AF_INET,SOCK_STREAM,0);
	address.sin_family=AF_INET;
	address.sin_port=htons(6800);
	inet_pton(AF_INET,"127.0.0.1",&address.sin_addr);
	connect(c_socket,(struct sockaddr *)&address,sizeof(address));
	printf("Enter file name: ");
	scanf("%s",fname);
	send(c_socket,fname,255,0);
	while(recv(c_socket,buf,s,0)>0)
		printf("%s",buf);
	return close(c_socket);
}
