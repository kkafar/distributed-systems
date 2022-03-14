package com.kkafara.demo;

import com.kkafara.demo.model.Todo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/todo")
public class TodoResourceSimple {
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String getHTML() {
    Todo todo = new Todo();
    todo.setSummary("First todo summary");
    todo.setDescription("First todo description");
    return "<p>Hello todo</p>";
  }

  @GET
  @Produces(MediaType.TEXT_XML)
  public Todo getXML() {
    Todo todo = new Todo();
    todo.setSummary("First todo summary");
    todo.setDescription("First todo description");
    return todo;
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String getPlainText() {
    return "some plain text";
  }

  @GET
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Todo getAppXML() {
    Todo todo = new Todo();
    todo.setSummary("First todo summary");
    todo.setDescription("First todo description");
    return todo;
//    return "<?xml version=\"1.0\"?>" + "<hello> Hello TODO" + "</hello>";
  }
}
