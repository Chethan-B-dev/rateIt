#include<iostream>
#include<stdio.h>
using namespace std;
struct node{
	int dist[20];
	int from[20];
}route[10];

int main(){
	int dm[20][20],n;
	cout<<"Enter the number of nodes: "<<endl;
	cin>>n;
	cout<<"Enter the distance matrix: "<<endl;
	for(int i=0;i<n;i++){
		for(int j=0;j<n;j++){
			cin>>dm[i][j];
			dm[i][i]=0;
			route[i].dist[j]=dm[i][j];
			route[i].from[j]=j;
		}
	}
	int flag;
	do{
		flag =0;
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				for(int k=0;k<n;k++){
					if((route[i].dist[j])>(route[i].dist[k]+route[k].dist[j])){
						route[i].dist[j]=route[i].dist[k]+route[k].dist[j];
						route[i].from[j]=k;
						flag=1;
					}
				}
			}
		}
	}while(flag);
	for(int i=0;i<n;i++){
		printf("\nRouter info for router: %d\n",i+1);
		printf("Dest.\tNext Hop\tDist.\n");
		for(int j=0;j<n;j++)
			printf("%d\t%d\t\t%d\n",j+1,route[i].from[j]+1,route[i].dist[j]);
	}
	return 0;
}

