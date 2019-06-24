package de.ls5.wt2.conf.auth;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;

import de.ls5.wt2.DBUser;
import de.ls5.wt2.DBUser_;

@Transactional
public class DatabaseAuthenticator {

    @PersistenceContext
    private EntityManager entityManager;

    public AuthenticationInfo fetchAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    	 final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
         final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
         UsernamePasswordToken token2 = (UsernamePasswordToken) token;

         final Root<DBUser> from = query.from(DBUser.class);
         query.where(builder.equal(from.get(DBUser_.name), (String) token.getPrincipal()),
                     builder.equal(from.get(DBUser_.password), String.valueOf(token2.getPassword()))		 
        		 );
         query.select(from);

         final List<DBUser> results = this.entityManager.createQuery(query).setMaxResults(1).getResultList();
    	
         if(results.size() == 0)
         {
        	 throw new AuthenticationException();
         }
    	
        return new SimpleAccount( results.get(0).getName(), results.get(0).getPassword().toCharArray(), WT2Realm.REALM);
    }

}