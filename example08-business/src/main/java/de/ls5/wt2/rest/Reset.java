package de.ls5.wt2.rest;

import de.ls5.wt2.DBNews;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/reset")
@Transactional
public class Reset {

    @PersistenceContext
    private EntityManager entityManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response reset() {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBNews> query = builder.createQuery(DBNews.class);
        final Root<DBNews> from = query.from(DBNews.class);
        query.select(from);

        final List<DBNews> result = this.entityManager.createQuery(query).getResultList();
        for (DBNews res: result) {
            entityManager.remove(res);
        }

        return Response.ok().build();
    }
}
