set ns [new Simulator]
$ns color 1 Red
$ns color 2 Green
set nt [open 7.tr w]
$ns trace-all $nt
set na [open 7.nam w]
$ns namtrace-all $na
set n0 [$ns node]
set n1 [$ns node]
set n2 [$ns node]
set n3 [$ns node]
set n4 [$ns node]
set n5 [$ns node]
$ns duplex-link $n0 $n2 10Mb 1ms DropTail
$ns duplex-link $n1 $n2 10Mb 1ms DropTail
$ns duplex-link $n2 $n3 1Mb 1ms DropTail
$ns duplex-link $n3 $n4 1Mb 1ms DropTail
$ns duplex-link $n3 $n5 2Mb 1ms DropTail
$ns queue-limit $n2 $n3 3
$ns queue-limit $n3 $n2 3
set Ping1 [new Agent/Ping]
set Ping2 [new Agent/Ping]
set Ping3 [new Agent/Ping]
set Ping4 [new Agent/Ping]
$ns attach-agent $n0 $Ping1
$ns attach-agent $n1 $Ping2
$ns attach-agent $n4 $Ping3
$ns attach-agent $n5 $Ping4
Agent/Ping instproc recv {from rtt} {
	$self instvar node_
	puts "Node[$node_ id] --> Node$from : RTT=$rtt ms"
}
$ns connect $Ping1 $Ping4
$ns connect $Ping2 $Ping3	
$Ping1 set class_ 1
$Ping2 set class_ 2
proc End {} {
	global ns nt na
	$ns flush-trace
	close $nt
	close $na
	exec nam 7.nam &
	exit 0
}
for {set t 0} {$t<5} {set t [expr $t+0.001]} {
	$ns at $t "$Ping1 send"
	$ns at $t "$Ping2 send"
}
$ns at 5.0 "End"
$ns run
