package capture_tool


import groovy.util.CliBuilder
import org.apache.commons.cli.Option

cli = new CliBuilder(usage: "${this.class.name} -z <zip file name> -o <OUTPUT LOG> -i <INPUT LOG> -f <FILTER> -t <TIME IN MINUTES>")
cli.with
{
	o(longOpt: 'output', 'specify the output log file', args: 1, type: String, required: true)
	t(longOpt: 'time', 'Time to run script in minutes', args: 1, type: Number.class, required: true)
	h(longOpt: 'help', 'Help', required:false)
	z(longOpt: 'zip', 'Zip up output log when complete', args: 1, type: String, required:false)
	i(longOpt: 'input', 'Log to monitor', args: 1, type: String, required: true)
	f(longOpt: 'filter', 'filter on string|regex', args: 1, type: String, required: true)
	//f(longOpt: 'strings', 'String pattern', args: Option.UNLIMITED_VALUES, valueSeparator: ',', required: true)


}

opt = cli.parse(args)

def timer,zip
if(opt.t)
{
	timer = opt.t
}
/*
println """
	Output log: ${opt.o}
	Log to Monitor: ${opt.i}
	Filter: ${opt.f}
	Time: ${opt.t}
	${opt.t.getClass()}
"""
*/


min = timer.toInteger()
long ms = min * 60000
c = "/users/netcool/groovy/groovy-2.3.6/bin/groovy /opt/netcool/omnibus/probes/wfotherscripts/capture_tool/log_capture.g ${opt.o.toString()} ${opt.i.toString()} ${opt.f}"
Process logcap = c.execute()
println "${this.class.name} is running. View the output here: ${opt.o}"
println "if ${opt.o} is not being updated, check your filter and log source"
logcap.consumeProcessOutput(System.out, System.err)
logcap.waitForOrKill(ms)
f = opt.o.toString()
outlog = new File(f)
if(opt.z && outlog.exists())
{

	Process zipit = "/usr/bin/zip ${opt.z} ${opt.o}".execute()
	zipit.waitFor()
	println "${zipit.err.text}\n"
	println "${zipit.in.text} exit value: ${zipit.exitValue()}"
	//Process rm = "/bin/rm ${opt.o}".execute()
	//rm.waitFor()
}
else
{

	println "${f} was not created. Are you sure ${opt.f.toString()} is a valid filter?\n"
}


