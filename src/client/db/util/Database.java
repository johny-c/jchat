package client.db.util;

import common.db.entity.AddContactRequest;
import common.db.entity.ChatMessage;
import common.db.entity.Contact;
import common.db.entity.Notification;
import common.db.entity.UserSession;
import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Database {

    public static void insertContact(Contact contactIn) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            //             String hql = "INSERT INTO Contact(firstName, lastName, salary)"
            //         + "SELECT firstName, lastName, salary FROM contactIn";

            String hql = "INSERT INTO Contact "
                    + "FROM contactIn";
            Query query = session.createQuery(hql);
            int result = query.executeUpdate();
            System.out.println("Rows affected: " + result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void updateContactStatus(Contact c) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "UPDATE Contact set status = :status "
                    + "WHERE id = :cid";
            Query query = session.createQuery(hql);
            query.setParameter("status", c.getStatus());
            query.setParameter("cid", c.getId());
            int result = query.executeUpdate();
            System.out.println("Rows affected: " + result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void deleteContact(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "DELETE FROM Contact "
                    + "WHERE id = :cid";
            Query query = session.createQuery(hql);
            query.setParameter("cid", id);
            int result = query.executeUpdate();
            System.out.println("Rows affected: " + result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void updateNotificationStatus(Notification n) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "UPDATE Notification set status = :status "
                    + "WHERE id = :nid";
            Query query = session.createQuery(hql);
            query.setParameter("status", n.getStatus());
            query.setParameter("nid", n.getId());
            int result = query.executeUpdate();
            System.out.println("Rows affected: " + result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static AddContactRequest findACRByNotificationId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        AddContactRequest acr = null;
        try {
            tx = session.beginTransaction();
            String hql = "from AddContactRequest where notificationId := nid";
            Query query = session.createQuery(hql);
            query.setParameter("nid", id);
            query.setMaxResults(1);
            acr = (AddContactRequest) query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return acr;
    }

    public static Integer findConversationIdByNotificationId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Integer convId = null;
        try {
            tx = session.beginTransaction();
            String hql = "from Conversation where notificationId := nid";
            Query query = session.createQuery(hql);
            query.setParameter("nid", id);
            query.setMaxResults(1);
            convId = (Integer) query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return convId;
    }

    public static Integer findFileIdByNotificationId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Integer fileId = null;
        try {
            tx = session.beginTransaction();
            String hql = "from FileTransfer where notificationId := nid";
            Query query = session.createQuery(hql);
            query.setParameter("nid", id);
            query.setMaxResults(1);
            fileId = (Integer) query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return fileId;
    }

    public static void updateChatMessageSGID(ChatMessage cmIn) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "UPDATE ChatMessage set serverGenId = :sgid "
                    + "WHERE clientGenId = :cgid";
            Query query = session.createQuery(hql);
            query.setParameter("sgid", cmIn.getServerGenId());
            query.setParameter("cgid", cmIn.getClientGenId());
            int result = query.executeUpdate();
            System.out.println("Rows affected: " + result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static ChatMessage findChatMessageByClientGenId(Integer clientGenId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        ChatMessage cm = null;
        try {
            tx = session.beginTransaction();
            String hql = "from ChatMessage where clientGenId := cgid";
            Query query = session.createQuery(hql);
            query.setParameter("cgid", clientGenId);
            query.setMaxResults(1);
            cm = (ChatMessage) query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return cm;
    }

    public static ChatMessage findChatMessageByServerGenId(Integer serverGenId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        ChatMessage cm = null;
        try {
            tx = session.beginTransaction();
            String hql = "from ChatMessage where serverGenId := sgid";
            Query query = session.createQuery(hql);
            query.setParameter("sgid", serverGenId);
            query.setMaxResults(1);
            cm = (ChatMessage) query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return cm;
    }

    // Works
    public Integer insert(Object obj) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Integer objectID = null;
        try {
            tx = session.beginTransaction();

            objectID = (Integer) session.save(obj);
            session.flush();
            session.clear();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return objectID;
    }

    /* Works, Method to  READ all the employees */
    public List<Object> select(Class className) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Object> objects = null;
        try {
            tx = session.beginTransaction();
            objects = session.createQuery("FROM " + className.getSimpleName()).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return objects;
    }

    public void delete(Object obj) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(obj);
            session.flush();
            session.clear();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void update(Object obj) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(obj);
            session.flush();
            session.clear();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void updateHQL(String hql, Map<String, Object> parameters) {
        // "update TableName" + " " + "set" + " " + "pname =" + " " + ":" + "pvalue" + ", "
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(hql);
            for (Map.Entry<String, Object> p : parameters.entrySet()) {
                query.setParameter(p.getKey(), p.getValue());
            }
            query.executeUpdate();
            session.flush();
            session.clear();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Object get(Integer id, Class className) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Object object = null;
        try {
            tx = session.beginTransaction();

            object = session.get(className, id);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return object;
    }

    // Works
    public void deleteAll(Class aClass) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "delete from " + aClass.getSimpleName() + " where 1=1";
            session.createQuery(hql).executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public UserSession getLastUserSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        UserSession object = null;
        try {
            tx = session.beginTransaction();

            object = (UserSession) session.createQuery("FROM UserSession").uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return object;
    }
}
