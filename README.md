htmlJgen --- HTML generation from plain Java
================================================

Create a simple DOM tree for HTML with Java

Also provides a way to handle servlet GET and POST parameters.

See the current <a href="http://haraldki.github.io/htmlJgen/htmlJgen-javadoc/">javadoc</a> for more information.

Test coverage reporting is also <a href="http://haraldki.github.io/htmlJgen/coverage-report/">available</a>.

## Why that?

I was always wondering what the point is with JSP? How it came about
is easy to understand. It is a way to have dynamic content inside
a static HTML page. To avoid the CPU cost of interpreting a
template language, JSP is compiled into Java classes.

But the HTML/JSP I work with contains 95% or more JSP tags, sprinkled with the
odd original &lt;div> here and there. In the worst case there are
scriptlets of pure Java contained, but it has nothing to do anymore
with "mostly HTML".

So it is all generated HTML anyway. It is programmed! But JSP is a
terribly designed "programming language" mixing aspects of HTML, XML,
Java scriptlets, expression language and the JSP itself. What a
hodge-podge.

## Rather use htmlJgen

For programming, we should use a decent programming language, properly
defined syntax, strongly typed, reusing code used in different places
by method calls, clean code and all that: Java. 

And of course we do not want to write sequences of `println(...)`
calls. *htmlJgen* is my attempt of a very simple class library to
create an HTML structure programmatically and send it nicely formatted
to the browser. It can be used as part of the servlet to create the
HTML.

You create your page template at some central place, for example, like


    final HtmlPage pageTemplate() {
      HtmlPage page = new HtmlPage("my cool app");
      page.addJs("/static/main.js");
      page.addCss("/static/main.css");
      page.addLink("image/png", "/static/favicon.png", "icon");
      ...
      return page;
    }

Later on you add content to the page, typically generators for `div`
elements:

    page.addContent(renderTable(results));

where `renderTable(List<Data> results)` creates, for example, a
table from result `Data` objects that were retrieved as part of the
servlet's operation:

    Html renderTable(List<Data> results) {
       Html div = new Html("div").setAttr("class", "resulttable");
       Html table = div.add("table");
       for(Data d : results) {
          table.add(renderResultRow(d));
       }
       return table;
    }

Finally you send the data out with a method somehow accessible by most
or all of your servlets, resembling:

    public void sendPage(HttpServletResponse resp, HtmlPage page) {    
      resp.setContentType("text/html");    
      resp.setCharacterEncoding("UTF-8");
    
      try {
        Writer w = resp.getWriter();
        page.print(w);
      } catch (IOException e) {
        LOG.error("could not write response body", e);
      }
    }

## Where is the HTML structure?

You cannot read the HTML structure anymore? Well, me neither. But
could you see it still in the last JSP you looked at? 


## Why are there no individual classes for each HTML element?

This would be nice indeed: encode all the rules of HTML into a class
structure such that it is impossible to put a &lt;div> into a
&lt;span> or add a *selected* attribute to a &lt;p> element.

But it is a daring task to get all of this complete and correct,
possibly catering for different HTML4, 4.01, 5 versions out
there. Feel free to prepare a pull request.-)

I even retracted the `EmptyElem.setClass(String classes)` method again
despite the fact that `setAttr("class", "yada")` is the
most frequently used call. But once you start with these convenience
methods it is hard to stop. And any stop would be arbitrary, so kept it
all clean and simple for now.



