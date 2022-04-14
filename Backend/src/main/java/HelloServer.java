import spark.Spark;

/** Simple server that just says hello. */
public class HelloServer {

  public static void main(String[] args) {
    // Get the person's name from the query string if available.
    Spark.get("/hello", (req, res) -> {
      String name = req.queryParams("name");
      return "Hello, " + (name != null ? name : "friend");
    });

    // Get the person's name from the URL.
    Spark.get("/hello/:name", (req, res) -> {
      return "Hello, " + req.params(":name");
    });
  }

}