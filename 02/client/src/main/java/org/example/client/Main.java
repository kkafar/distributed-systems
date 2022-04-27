package org.example.client;

import org.example.client.model.Todo;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class Main {
  private final static String SERVER_AUTHORITY = "localhost";
  private final static String SERVER_ROOT = "demo";
  private final static String SERVER_HELLO_ENDPOINT = "hello";
  private final static String SERVER_TODO_ENDPOINT = "todo";
  private final static String SERVER_TODOS_ENDPOINT = "todos";

  private final static int SERVER_PORT = 4444;

  public static void main(String[] args) {
//    clientEx1(args);
//    clientEx2(args);
    clientEx4(args);
  }

  public static void clientEx1(String[] args) {
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target(getBaseURI());
    // Fluent interfaces
    System.out.println(target.path("rest").path(SERVER_HELLO_ENDPOINT).request()
        .accept(MediaType.TEXT_PLAIN).get(Response.class).toString());
    // Get plain text
    System.out.println(target.path("rest").path(SERVER_HELLO_ENDPOINT).request()
        .accept(MediaType.TEXT_PLAIN).get(String.class));
    // Get XML
    System.out.println(target.path("rest").path(SERVER_HELLO_ENDPOINT).request()
        .accept(MediaType.TEXT_XML).get(String.class));
    // Get HTML
    System.out.println(target.path("rest").path(SERVER_HELLO_ENDPOINT).request()
        .accept(MediaType.TEXT_HTML).get(String.class));
  }


  public static void clientEx2(String[] args) {
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target(getBaseURI());
// Fluent interfaces
    System.out.println(target.path("rest").path(SERVER_TODO_ENDPOINT).request()
        .accept(MediaType.TEXT_XML).get(Response.class).toString());
// Get XML
    System.out.println(target.path("rest").path(SERVER_TODO_ENDPOINT).request()
        .accept(MediaType.TEXT_XML).get(String.class));
// Get XML for application
    System.out.println(target.path("rest").path(SERVER_TODO_ENDPOINT).request()
        .accept(MediaType.APPLICATION_XML).get(String.class));
// Get JSON for application
    System.out.println(target.path("rest").path(SERVER_TODO_ENDPOINT).request()
        .accept(MediaType.APPLICATION_JSON).get(String.class));
  }

  public static void clientEx4(String[] args) {
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target(getBaseURI());

    System.out.println(target.path("rest").path(SERVER_TODOS_ENDPOINT).request(MediaType.TEXT_XML).get(String.class));

    System.out.println(target.path("rest").path(SERVER_TODOS_ENDPOINT).request(MediaType.APPLICATION_JSON).get(String.class));

    System.out.println(target.path("rest").path(SERVER_TODOS_ENDPOINT).request(MediaType.APPLICATION_XML).get(String.class));

    // put new "todo"
    Todo newTodo = new Todo("mock id", "mock summary");
    Response response = target.path("rest").path(SERVER_TODOS_ENDPOINT).path(newTodo.getId()).request(MediaType.APPLICATION_XML)
        .put(Entity.xml(newTodo));

    System.out.println("SERVER RESPONSE");
    System.out.println(response.getStatus());
    System.out.println(response.getStatusInfo().toString());

    // Get the Todo with id 1
    System.out.println(target.path("rest").path("todos/1")
        .request(MediaType.APPLICATION_XML).get(String.class));
    // Delete the Todo with id 1
    target.path("rest").path("todos/1").request().delete();
    System.out.println(response.getStatus());
    System.out.println(response.getStatusInfo().toString());
    // Get the all todos, id 1 should be deleted
    System.out.println(target.path("rest").path("todos")
        .request(MediaType.APPLICATION_XML).get(String.class));

    Form form = new Form();
    form.param("id", "4");
    form.param("summary", "Demonstration of the client lib for forms");
    response = target.path("rest").path("todos")
        .request()
        .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
    System.out.println("Form response: " + response.readEntity(String.class));
// Get the all todos, id 4 should be created
    System.out.println(target.path("rest").path("todos")
        .request(MediaType.APPLICATION_XML).get(String.class));
  }

  private static URI getBaseURI() {
    return UriBuilder.fromUri("http://" + SERVER_AUTHORITY + ":" + SERVER_PORT + "/" + SERVER_ROOT).build();
  }
}
