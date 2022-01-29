set ns [new Simulator]
set nt [open 8.tr w]
$ns trace-all $nt
set na [open 8.nam w]
$ns namtrace-all $na
$ns color 0 blue

set n0 [$ns node]
set n1 [$ns node]
set n2 [$ns node]
set n3 [$ns node]
set n4 [$ns node]
set n5 [$ns node]
set n6 [$ns node]
set n7 [$ns node]

$n0 color "red"
$n1 color "red"
$n2 color "red"
$n3 color "red"
$n4 color "magenta"
$n5 color "magenta"
$n6 color "magenta"
$n7 color "magenta"

$ns make-lan "$n0 $n1 $n2 $n3" 100Mb 300ms LL Queue/DropTail Mac/802_3
$ns make-lan "$n4 $n5 $n6 $n7" 100Mb 300ms LL Queue/DropTail Mac/802_3
$ns duplex-link $n3 $n4 100Mb 300ms DropTail
$ns duplex-link-op $n3 $n4 color "green"

set err [new ErrorModel]
$ns lossmodel $err $n3 $n4
$err set rate_ 0.1
set UDP [new Agent/UDP]
$ns attach-agent $n1 $UDP

set CBR [new Application/Traffic/CBR]
$CBR attach-agent $UDP
$CBR set fid_ 0
$CBR set packetSize_ 1000
$CBR set interval_ 0.0001
set NULL [new Agent/Null]
$ns attach-agent $n7 $NULL
$ns connect $UDP $NULL

proc finish {} {
    global ns na nt
    $ns flush-trace
    close $na
    close $nt
    exec nam 8.nam &
    exit 0
}
$ns at 0.1 "$CBR start"
$ns at 3.0 "finish"
$ns run