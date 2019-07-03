package de.ls5.wt2.rest;

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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import de.ls5.wt2.DBNews;
import de.ls5.wt2.DBNews_;
import de.ls5.wt2.DBUser;
import de.ls5.wt2.DBUser_;

@Path("/news")
@Transactional
public class NewsCRUD {

    @PersistenceContext
    private EntityManager entityManager;

    @GET
    @Path("/newest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readNewestNews() {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBNews> query = builder.createQuery(DBNews.class);

        final Root<DBNews> from = query.from(DBNews.class);

        final Order order = builder.desc(from.get(DBNews_.publishedOn));

        query.select(from).orderBy(order);

        return Response.ok(this.entityManager.createQuery(query).setMaxResults(1).getSingleResult()).build();
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

        return Response.ok(this.entityManager.createQuery(query).setMaxResults(1).getSingleResult()).build();
    }
    
    @POST
    @Path("/newAuthor")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewAuthor(final DBUser param) {
    	final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
    	final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
    	
    	final Root<DBUser> from = query.from(DBUser.class);
    	
    	query.where(builder.equal(from.get(DBUser_.name), param.getName()));
    	query.select(from);
    	
    	final List result = this.entityManager.createQuery(query).getResultList();
    	if(result.size() > 0)
    	{
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}
    	
    	final DBUser user = new DBUser();
    	user.setName(param.getName());
    	user.setPassword(param.getPassword());
    	
    	this.entityManager.persist(user);
    	
    	return Response.ok(user).build();
    }
    

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DBNews> readAllAsJSON() {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBNews> query = builder.createQuery(DBNews.class);

        final Root<DBNews> from = query.from(DBNews.class);

        query.select(from);

        return this.entityManager.createQuery(query).getResultList();
    }
    
    @POST
    @Path("byAuthor")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response readNewsByAuthor(final DBNews param) {
    	 final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
         final CriteriaQuery<DBNews> query = builder.createQuery(DBNews.class);

         final Root<DBNews> from = query.from(DBNews.class);
         
         query.where(builder.equal(from.get(DBNews_.author), param.getAuthor()));
         
         query.select(from);
         final List result = this.entityManager.createQuery(query).getResultList();
         return Response.ok(result).build();
    }

    @Path("/{id}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public DBNews readAsJSON(@PathParam("id") final long id) {
        return this.entityManager.find(DBNews.class, id);
    }

}
