package capture_tool

assert args.size() == 3 : "\n####### NOT ENOUGH ARGUMENTS\nUsage: log_capture.g <OUTPUT LOG> <LOG TO WATCH> <FILTER> #######"
capturelog = args[0].toString()
fileorpipe = args[1].toString()
filter = args[2].toString()
if(filter=~/~/)
{
    filter=filter.replaceAll('~',' ')
}
/*
println """
    capturelog = ${capturelog}
    fileorpipe = ${fileorpipe}
    filter = ${filter}
"""
*/


instream = new InputStreamReader(new FileInputStream("${fileorpipe}"), "utf-8" )
reader = new BufferedReader(instream)
cap = new File(capturelog)
rx = ~/$filter/
while(1)
//while ((s = reader.readLine())&& cap.size() < 10000000)
{
    s = reader.readLine()
    if((matcher = s.toString() =~ rx ))
    {
        cap << s + "\n"
    }
}