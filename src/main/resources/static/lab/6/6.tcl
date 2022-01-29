set ns [new Simulator]
set nt [open 6.tr w]
$ns trace-all $nt
set na [open 6.nam w]
$ns namtrace-all $na
set n0 [$ns node]
set n1 [$ns node]
set n2 [$ns node]
set n3 [$ns node]
$ns duplex-link $n0 $n2 10Mb 1ms DropTail
$ns duplex-link $n2 $n3 10Mb 1ms DropTail
$ns duplex-link $n2 $n1 10Mb 1ms DropTail
$ns queue-limit $n0 $n2 10
$ns queue-limit $n1 $n2 10
set tcp [new Agent/TCP]
$ns attach-agent $n0 $tcp
set sink [new Agent/TCPSink]
$ns attach-agent $n3 $sink
set ftp [new Application/FTP]
$ftp attach-agent $tcp
set udp [new Agent/UDP]
$ns attach-agent $n2 $udp
set null [new Agent/Null]
$ns attach-agent $n3 $null
set cbr [new Application/Traffic/CBR]
$cbr attach-agent $udp
$ns connect $tcp $sink
$ns connect $udp $null
proc finish {} {
global ns nt na
$ns flush-trace
close $nt
close $na
exec nam 6.nam &
exit 0
}
$ns at 0.0 "$ftp start"
$ns at 0.2 "$cbr start"
$ns at 0.8 "finish"
$ns run
