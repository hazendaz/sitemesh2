--------------------------
-- Changes in 2.5       --
--------------------------
 * Single buffer parsing. This is supported by the HTMLPageParser and the PartialPageParser, and means
   that the parsing process will never copy the content into another buffer, thus increasing
   performance and reducing the amount of garbage the garbage collector has to clean up after a parsing
 * Buffer chaining. This allows multiple nested decorators (for example, using the JSP tags) to not
   need to rebuffer or reparse the HTML of the chained buffer.
 * PartialPageParser that only parses the <head> section of bodies. This is a very high performance
   buffer for simple sitemesh use cases, that don't need any content extracted from the body. It stops
   parsing as soon as it reaches the first body tag, and then scans from the end of the content to find
   the end body tag, and treats everything in between as the body.  Does not allow many of the advanced
   features of Sitemesh to be used, such as filters and and <content> tags.  To use the partial page
   parser, set the the pageParser in sitemesh.xml to be com.opensymphony.module.sitemesh.parser.PartialPageParser
 * Relicense project as apache 2
 * Move ownership from sitemesh to hazendaz
 * Migrate from ant to maven build
 * Extensive code cleanup (same behaviour - no new features)
 * [upstream PR] Replaced @VERSION@ with <VERSION> because Github readme was rendering it as email to VERSION@.jar 
 * [upstream PR] Changed how files are loaded in DefaultFactory and Fixes issue #18
 * [upstream PR] support for decorator:div tag
 * [upstream PR] Weblogic 10.3.5 unable to find excludesFile path using getResourceAsStream (alternative approach if null only)
 * [upstream PR] fix rendering for script/comment/CDATA inside content tags in FastPageParser.java
 * [upstream PR] com.opensymphony.module.sitemesh.Factory no longer works with Java 11
 * Removed code for java 1.3 and before while fixing encoding to properly work for all others

--------------------------
-- Changes in 2.4.2     --
--------------------------
Minor update to allow SiteMesh to initialize correctly when deployed on Google AppEngine.

--------------------------
-- Changes in 2.4.1     --
--------------------------
Fixed error in Page.getRequest() implementation. Now deprecated and removed from examples.

--------------------------
-- Changes in 2.4       --
--------------------------
Mostly minor fixes.

Bug fixes
    * [SIM-156] - Style Tags are stripped from <HEAD>
    * [SIM-172] - Unclosed quotes cause parsing problems
    * [SIM-180] - Header content after a <content> tag gets treated as part of the body
    * [SIM-186] - SM should honor (at least) 304 (SC_NOT_MODIFIED)
    * [SIM-197] - when bodyContent is null,page:applyDecorator will throw NullPointerException
    * [SIM-198] - Deleted '<' in included content
    * [SIM-206] - XML Declaration in XHTML
    * [SIM-216] - NoSuchElementException in com.opensymphony.module.sitemesh.html.HTMLProcessor$1.currentBuffer(HTMLProcessor.java:44)
    * [SIM-241] - ArrayIndexOutofBounds exception in HTMLPageParser
    * [SIM-247] - OSDecoratorMapper == FAIL
    * [SIM-249] - tlds are broken - shortname does not conform to NMTOKEN
Improvements
    * [SIM-151] - Allow the forced session creation to be optional
    * [SIM-174] - Simple (but measurable) Performance improvements for DefaultFactory and ConfigLoader
    * [SIM-192] - Need more user-friendly log error message when decorators.xml is missing
Tasks
    * [SIM-229] - Update Sitemesh testsuite to use Cargo
    * [SIM-230] - Remove PageFilter, in favour of new SiteMeshFilter

--------------------------
-- Changes in 2.3       --
--------------------------
Default parser has been moved from FastPageParser to HTMLPageParser.
Misc bug fixes.
Added components to enable decorators to be built with Tapestry (contributed by Erik Hatcher). 

--------------------------
-- Changes in 2.2.1     --
--------------------------  
SiteMesh 2.2 introduced a few critical bugs, which have stopped it
from being a seamless upgrade to many users.

-- Fixed critical bugs --

* ClassNotFound: DefaultPageParser
This is a class that no longer exists but may still lurk in
sitemesh.xml if based on a 1.0 version. The config loader now just
silently ignores this.

* Head of content page chopped off
This was caused by setContentType() always resetting the buffer,
however sometimes setContentType() was called half way through a page
(for instance by the Servlet engine whilst doing an include).

--------------------------
-- Changes in 2.2       --
--------------------------
This release fixes a number of minor bugs, and no code changes are required
from 2.1. 

The following improvements have been made:
    - The <excludes> tag in decorators.xml now takes into account ServletPath, PathInfo and QueryString.
    - Overhaul of the main Servlet Filter to remove unnecessary complexity and more gracefully handle situations
      where the order of calls on the HttpServletResponse, PrintWriter and ServletOutputStream occur in an 
      awkward order.

JIRA issues fixed:
    - SIM-125 Internal Tomcat CGI throws NullPointerException.
    - SIM-128 HTML parser cannot handle attributes that span multiple lines.
    - SIM-86  Requests that use setHeader("Content-type") instead of setContentType() do not get decorated.
    - SIM-134 Not calling setContentType() caused NullPointerException.


--------------------------
-- Changes in 2.1       --
--------------------------
This release fixes a number of major bugs, and no code changes are required
from 2.0.1.  We recommend all users upgrade.

This release mainly contains application server compatibilty changes. 
Sitemesh is now compatible with more servers than ever before, and this
release fixes the number one bug which was decoration of static pages under 
Tomcat.

- The URI's for the SiteMesh tag libraries have changed:

    sitemesh-decorator becomes   http://www.opensymphony.com/sitemesh/decorator
    sitemesh-page      becomes   http://www.opensymphony.com/sitemesh/page

  There have been no other changes to the tag library descriptors (.tld) files from
  the previous version, so you don't need to replace you existing ones.

- Added VelocityDecoratorServlet (SIM-62; see 'Velocity Decorators' in the documentation and the example webapp).
- The example webapp and blank webapp use Packaged Tag Libraries (specifying the URI of the taglib).
- Hardened ConfigLoader to ignore whitespace inserted by XMLBuddy.
- A new method, isPathExcluded(), has been added to the Factory interface (SIM-98)
- There is no longer the concept of a default parser for unknown content types.

JIRA issues fixed:
    - SIM-82  IllegalStateException when decorating static pages in Tomcat 4 & 5
    - SIM-114 Sitemesh truncates content on non-decorated pages.
    - SIM-73  PageFilter is not final and you can now subclass the newly protected applyDecorator and parsePage methods
    - SIM-83  RequestDispatcher.forward() support
    - SIM-91  Remove singleton configuration
    - SIM-55  applyDecorator tag doesn't work on resin
    - SIM-73  Make PageFilter subclassable
    - SIM-89  <url-pattern> mapping on deployment descriptor
    - SIM-97  Sitemesh captures the response even for non-parseable content
    - SIM-98  Add the ability to exclude content from being decorated

--------------------------
-- Changes from 2.0     --
--------------------------

Chris Miller has been working like a demon on FastPageParser, and it's now as
fast and efficient as it can get. To give you a rough idea, 2.0 is about 3 times
faster than 1.5. 2.0.1 is about 5 times faster than 1.5.

With regards to memory usage, it's basically been knocked down to be negligible.
Previously (1.5), a 50k page parsed 250 times (separate instances strongly
referenced, with an explicit gc call to remove temporary objects) used up 37mb.
Currently, it uses 25mb (and it's no coincidence that 50k * 250 * 2 bytes per
char == 25mb).

- Minor DTD fix.
- Updates to documentation and build process.
- Added ParserGrinder to load test FastPageParser.

--------------------------
-- Changes from 1.5     --
--------------------------
- SiteMesh now hosted at http://sitemesh.dev.java.net.
- FastPageParser performance improvements.
- DTD location has changed; now http://www.opensymphony.com/sitemesh/dtd/sitemesh_1_5_decorators.dtd
- API change in Decorator (check your custom written Decorator classes):
  Added new method getRole() to enable role based decorators.
- Updated documentation to align with new Opensymphony website.

JIRA issues fixed:
    SIM-16 Tomcat IllegalStateException
    SIM-41 NoSuchMethodException with Orion
    SIM-2  Response bug on WebLogic 6.1
    SIM-13 Tomcat4 throws IOException after response.sendRedirect()
    SIM-29 WebLogic 7 doesn't work
    SIM-40 Let properties be retrieved programatically
    SIM-27 Example apps don't work in Pramati
    SIM-17 Can't set headers from decorator page.
    SIM-8  body tag not correctly parsed
    SIM-56 Decorator taglibs allowed to contain body
    SIM-37 role based decorators
    SIM-46 Place TLDs in Jar file.

--------------------------
-- Changes from 1.4.1   --
--------------------------
- API change in DecoratorMapper (check your custom written DecoratorMappers):
  before
        Decorator getNamedDecorator(String name);
  after
        Decorator getNamedDecorator(HttpServletRequest request, String name);

- API change in Decorator (check your custom written Decorator classes):
  Added new method getURIPath() to enable cross web-app support for decorators.

- New (shorter!) decorator xml format (backward compability is maintained),
  check above or the decorators.xml file in the /example/WEB-INF directory for an example.
  DTD: http://www.opensymphony.com/sitemesh/dtd/sitemesh_1_5_decorators.dtd
- Default SiteMesh configuration if sitemesh.xml is not present.
- When the request contains a Page object (under the key RequestConstants.PAGE)
  use this one (supports SiteMesh aware applications) so we don't need to parse.
- EnvEntryDecoratorMapper: allows the reference to a web-app environment entry for the
  decorator name, and falls back to ConfigDecoratorMapper's behavior if no matching
  environment entry is found.
- Cross web-app support for decorators by specifying <decorator ... webapp="anotherwebapp"/>.
  This will first try to get the decorator from anotherwebapp and fall back if not found.
- Small performance improvements in RobotDecoratorMapper.
- Some improvements to get SiteMesh working on different web containers.
  If you need to detect on which container your application is running,
  have a look at the com.opensymphony.module.sitemesh.util.Container class.

- BUGFIX: DefaultDecorator returned bad init param
- BUGFIX: small fixes to make SiteMesh work better on Tomcat
- BUGFIX: javadoc fixes

JIRA issues fixed:
    SIM-1  Finalize RE support in PathMapper
    SIM-3  Page filter strips <xmp> tags
    SIM-4  PathMapper order is incorrect
    SIM-5  Add a mapper that uses environment entries
    SIM-6  When using the EnvEntryDecoratorMapper, decorations fail for html
    SIM-7  Parsing of body should strip doctype
    SIM-8  body tag not correctly parsed
    SIM-11 Parsing the text "<>" causes problems
    SIM-16 Define how charsets should work
    SIM-20 Cross web-app support for decorators
    SIM-21 FactoryException thrown when starting testsuite on WebLogic
    SIM-22 Create template web-app
    SIM-23 Distribution bundle
    SIM-32 A smaller than sign (<) in javascript fails

--------------------------
-- Changes from 1.4     --
--------------------------

- complete support for WebLogic 6.1 / 7
- complete support for Jetty 4
- internal optimizations to filter

- BUGFIX: StringIndexOutOfBoundsException in RobotDecoratorMapper

--------------------------
-- Changes from 1.3     --
--------------------------

- changed package structure from com.sitemesh to com.opensymphony.module.sitemesh
- many performance optimizations in FastPageParser
- various bug fixes and small performance improvements

- BUGFIX: memory leak in PageFilter
- BUGFIX: meta http-equiv tags are now added as properties with prefix meta.http-equiv.x where x
  is the value of the http-equiv attribute (eg refresh)
- BUGFIX: the configuration files are not case-sensitive anymore


