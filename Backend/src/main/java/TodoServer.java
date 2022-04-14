import spark.Spark;

/**
 * Main class that sets up the to-do server, which allows users to read and
 * update a to-do list.
 */
public class TodoServer {

  public static void main(String[] args) {
    //new CORSFilter().apply();  // make this easier to use in development
                               // this would be disabled in production

    // Stores the current to-do list (just in memory... not saved anywhere).
    TodoList list = new TodoList(5);  // 5 seconds just for demo

    // Return the current list with one item per line.
    Spark.get("/list", (req, res) -> {
      res.type("text/plain");
      return list.describe();
    });

    Spark.get("/add/:name", (req, res) -> {
      String name = req.params(":name");
      if (name == null) {
        res.status(400);
        return "name missing";
      } else {
        res.type("text/plain");
        if (!list.has(name)) {
          list.add(name);
          return "added";
        } else {
          return "added already";
        }
      }
    });

    Spark.get("/completed/:name", (req, res) -> {
      String name = req.params(":name");
      if (name == null) {
        res.status(400);
        return "name missing";
      } else if (!list.has(name)) {
        res.status(400);
        return "item not found";
      } else {
        list.completed(name);
        return "done";
      }
    });


    // Adds the given item (?name=...) if not already present.
    Spark.post("/add", (req, res) -> {
      String name = req.queryParams("name");
      if (name == null) {
        res.status(400);
        return "name missing";
      } else {
        res.type("text/plain");
        if (!list.has(name)) {
          list.add(name);
          return "added";
        } else {
          return "added already";
        }
      }
    });

    // Marks the given item as completed (right now).
    Spark.post("/completed", (req, res) -> {
      String name = req.queryParams("name");
      if (name == null) {
        res.status(400);
        return "name missing";
      } else if (!list.has(name)) {
        res.status(400);
        return "item not found";
      } else {
        res.type("text/plain");
        list.completed(name);
        return "done";
      }
    });
  }

}