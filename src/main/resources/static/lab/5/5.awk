BEGIN{Count=0;}
{
	if($1=="d")
		Count++;
}
END{
	printf("\nNumber of packets dropped is: %d\n",Count);
}
