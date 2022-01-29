BEGIN{
d=0;
tcp=0;
udp=0;
pkt_t=0;
time_t=0;
pkt_u=0;
time_u=0;
}

{
if(($1=="r" && $3=="0" && $4=="2" && $5=="tcp")||($1=="r" && $3=="2" && $4=="3" && $5=="tcp")){
pkt_t=pkt_t+$6;
time_t=$2;
#printf("%f\t%f\n",pkt_t,time_t);
}

if(($1=="r" && $3=="1" && $4=="2" && $5=="cbr")||($1=="r" && $3=="2" && $4=="3" && $5=="cbr")){
pkt_u=pkt_u+$6;
time_u=$2;
#printf("%f\t%f\n",pkt_u,time_u);
}
}

END{
printf("Throughput of TCP: %f Mbps\n",((pkt_t/time_t)*(8/1000000)));
printf("Throughput of UDP: %f Mbps\n",((pkt_u/time_u)*(8/1000000)));
}
