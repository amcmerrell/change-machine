import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    ChangeMachine myChangeMachine = new ChangeMachine();

    String layout = "templates/layout.vtl";

    get("/form", (request, response) -> {
     Map<String, Object> model = new HashMap<String, Object>();
     model.put("template", "templates/form.vtl");
     return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/combinations", (request, response) -> {
     Map<String, Object> model = new HashMap<String, Object>();
     Float myNumber = Float.parseFloat(request.queryParams("amount"));
     if (checkTotal(myNumber)){
       model.put("amount", myChangeMachine.makeChange(myNumber));
     }else{
       model.put("amount", "Your Amount is too high for making change. Sorry!");
     }

     model.put("template", "templates/combinations.vtl");
     return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
  public static boolean checkTotal(Float myNumber) {
    ChangeMachine testMachine = new ChangeMachine();
    testMachine.makeChange(myNumber);
    Float myQuarterValue = (20 - testMachine.getQuarters()) * .25f;
    Float myDimeValue = (20 - testMachine.getDimes()) * .10f;
    Float myNickelValue = (20 - testMachine.getNickels()) * .05f;
    Float myPennyValue = (20 - testMachine.getPennies()) * .01f;
    return ((int)((myQuarterValue + myDimeValue + myNickelValue + myPennyValue)*100) == (int)(myNumber*100));
  }
}
