import com.sun.btrace.annotations.*;
import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class MethodTimeCost {
  @TLS private static long starttime;

  @OnMethod(clazz="/com\\.http\\.testbtrace\\..*/",method="/.+/",location=@Location(Kind.ENTRY))
  public static void startExecute(){
     starttime = timeMillis();
   }

  @OnMethod(clazz="/com\\.http\\.testbtrace\\..*/",method="/.+/",location=@Location(Kind.RETURN))
  public static void endExecute(){
      long timecost = timeMillis() - starttime;
      if(timecost > 50){
        print(strcat(strcat(name(probeClass()), "."), probeMethod()));
        print("   [");
        print(strcat("Time taken : ", str(timecost)));
        println("]");
      }

   }
}
