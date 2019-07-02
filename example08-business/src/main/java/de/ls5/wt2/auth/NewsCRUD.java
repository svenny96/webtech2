package de.ls5.wt2.auth;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.ls5.wt2.DBNews;
import de.ls5.wt2.DBNews_;
import de.ls5.wt2.conf.auth.permission.ViewFirstFiveNewsItemsPermission;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;

@Path("/auth/{a:session|basic|jwt}/news")
@Transactional
public class NewsCRUD {

    @PersistenceContext
    private EntityManager entityManager;

    @GET
    @Path("newest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readNewestNews() {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBNews> query = builder.createQuery(DBNews.class);

        final Root<DBNews> from = query.from(DBNews.class);

        final Order order = builder.desc(from.get(DBNews_.publishedOn));

        query.select(from).orderBy(order);

        final List<DBNews> results = this.entityManager.createQuery(query).setMaxResults(1).getResultList();
        if (results.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();


//        // Attribute based permission check using permissions
//        final Subject subject = SecurityUtils.getSubject();
//        final Permission firstFiveNewsItemsPermission = new ViewFirstFiveNewsItemsPermission(result);
//
//        if (!subject.isPermitted(firstFiveNewsItemsPermission)) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }

        return Response.ok(results.get(0)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response readAllAsJSON() {
        final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBNews> query = builder.createQuery(DBNews.class);

        final Root<DBNews> from = query.from(DBNews.class);

        query.select(from);

        final List result = this.entityManager.createQuery(query).getResultList();
        return Response.ok(result).build();
    }
    
    @POST
    @Path("byAuthor")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response readNewsByAuthor(final DBNews param) {
    	 final Subject subject = SecurityUtils.getSubject();
    	 final String subjectName = String.valueOf(subject.getPrincipal());
    	 
         if (subject == null || !subject.isAuthenticated()) {
        	 
             return Response.status(Response.Status.UNAUTHORIZED).build();
         }
         System.out.println("User2: "+String.valueOf(SecurityUtils.getSubject().getPrincipal()));
    	 final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
         final CriteriaQuery<DBNews> query = builder.createQuery(DBNews.class);

         final Root<DBNews> from = query.from(DBNews.class);
         
         query.where(builder.equal(from.get(DBNews_.author), param.getAuthor()));
         
         query.select(from);
         final List result = this.entityManager.createQuery(query).getResultList();
         return Response.ok(result).build();
    }
    
    @POST
    @Path("/newest/byAuthor")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readNewestNewsByAuthor(final DBNews param) {
    	final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBNews> query = builder.createQuery(DBNews.class);

        final Root<DBNews> from = query.from(DBNews.class);

        final Order order = builder.desc(from.get(DBNews_.publishedOn));
        
        query.where(builder.equal(from.get(DBNews_.author), param.getAuthor()));
        query.select(from).orderBy(order);

        final List<DBNews> results = this.entityManager.createQuery(query).setMaxResults(1).getResultList();
        if (results.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(results.get(0)).build();
    }
    
    @POST
    @Path("deleteNews")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteNews(final DBNews param) {
    	
    	final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
    	final CriteriaQuery<DBNews> query = builder.createQuery(DBNews.class);
    	
    	final Root<DBNews> from = query.from(DBNews.class);
    	
    	query.where(builder.equal(from.get(DBNews_.headline), param.getHeadline()),
    			    builder.equal(from.get(DBNews_.content), param.getContent()),
    			    builder.equal(from.get(DBNews_.author), param.getAuthor())
    			);
    	query.select(from);
    	
    	long newsId = this.entityManager.createQuery(query).getSingleResult().getId();
    	this.entityManager.remove(this.entityManager.find(DBNews.class, newsId));
    	
    	final CriteriaQuery<DBNews> query1 = builder.createQuery(DBNews.class);

        final Root<DBNews> from1 = query1.from(DBNews.class);
        
        
        query1.select(from1);
        final List result = this.entityManager.createQuery(query1).getResultList();
        return Response.ok(result).build();
  
    }
    
    @POST
    @Path("changeNews")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeNews(final DBNews param) {
    	System.out.println(param.getHeadline());
    	System.out.println(param.getContent());
    	
    	final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
    	final CriteriaQuery<DBNews> query = builder.createQuery(DBNews.class);
    	
    	final Root<DBNews> from = query.from(DBNews.class);
    	
    	query.where(builder.equal(from.get(DBNews_.headline), param.getHeadline().split("@")[0]),
    			    builder.equal(from.get(DBNews_.content), param.getContent().split("@")[0]),
    			    builder.equal(from.get(DBNews_.author), param.getAuthor())
    			);
    	query.select(from);
    	long newsId = this.entityManager.createQuery(query).getSingleResult().getId();
    	
    	 DBNews entity = this.entityManager.find(DBNews.class, newsId);
    	 
    	 entity.setHeadline(param.getHeadline().split("@")[1]);
    	 entity.setContent(param.getContent().split("@")[1]);
    	
    	 final CriteriaQuery<DBNews> query1 = builder.createQuery(DBNews.class);

         final Root<DBNews> from1 = query1.from(DBNews.class);
         
         
         
         query1.select(from1);
         final List result = this.entityManager.createQuery(query1).getResultList();
         return Response.ok(result).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    // Shiro annotations only work with byte-code weaving enabled.
    // This currently requires Java < 11. See https://github.com/mojohaus/aspectj-maven-plugin/pull/45
    @RequiresAuthentication
    @RequiresRoles("admin")
    public Response create(final DBNews param) {

        final DBNews news = new DBNews();

        news.setHeadline(param.getHeadline());
        news.setContent(param.getContent());
        news.setPublishedOn(new Date());
        news.setAuthor(param.getAuthor());

        this.entityManager.persist(news);
        
        return Response.ok(news).build();
    }
}
