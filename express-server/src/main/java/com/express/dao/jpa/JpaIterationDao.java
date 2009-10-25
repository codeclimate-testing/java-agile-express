package com.express.dao.jpa;

import com.express.dao.IterationDao;
import com.express.domain.Iteration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;

@Repository("iterationDao")
public class JpaIterationDao extends JpaGenericDao<Iteration> implements IterationDao {

   @Autowired
   public JpaIterationDao(EntityManagerFactory entityManagerFactory) {
      super(Iteration.class);
      super.setEntityManagerFactory(entityManagerFactory);
   }

   public List<Iteration> findOpenIterations() {
      Map<String, Calendar> params = new HashMap<String, Calendar>();
      params.put("date", Calendar.getInstance());
      return this.getJpaTemplate().findByNamedQueryAndNamedParams(Iteration.QUERY_FIND_OPEN,params);
   }

}
