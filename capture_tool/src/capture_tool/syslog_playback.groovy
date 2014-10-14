package capture_tool

// COMMENT:  syslog4j-0.9.46-bin.jar (add this jar to $GROOVY_HOME/lib)
import org.productivity.java.syslog4j.*
import groovy.util.CliBuilder
def ms = 0
cli = new CliBuilder(usage: "${this.class.name}-c <capture file> -s <hostname>")
cli.with
{
	s(longOpt: 'server', 'server to send messages to', args: 1, type: String, required: false)
	c(longOpt: 'capture', 'source capture file', args: 1, type: String, required: true)
	i(longOpt: 'interval', 'milliseconds between each sent message', args: 1, type: String, required: false)
	h(longOpt: 'help', 'Help', required:false)
}

opt = cli.parse(args)

SyslogIF syslog
syslog = Syslog.getInstance("udp");
if(opt.i)
{
	ms = opt.i
}
if(opt.s)
{
	syslog.getConfig().setHost(opt.s);
}
else
{
	syslog.getConfig().setPort(514);
}


def cnt = 0
// use below for floding probe with messages
def src = opt.c
new File(src).eachLine { msg ->
	msg = msg.toString().trim()
	syslog.info("${msg}")
	sleep(ms.toInteger())
	cnt++
}
println "sent ${cnt} messages"

