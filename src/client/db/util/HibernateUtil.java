/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.db.util;

import client.networking.R;
import common.utils.Utils;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author johny
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    private static final String DRIVER_URL = "jdbc:h2:";
    private static final String CLIENT_DB_FOLDER_NAME = "db_folder";
    private static final String CLIENT_DB_FILE_NAME = "jchat_cdb";
    private static String connUrl;

    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) config file.
            //sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();

            // Customized configuration of SessionFactory
            connUrl = DRIVER_URL + Utils.getAppDir()
                    + "/" + CLIENT_DB_FOLDER_NAME
                    + "/" + CLIENT_DB_FILE_NAME + ";";  //+ DB_SETTINGS;

            // First create the normal configuration from hibernate.cfg.xml
            Configuration cfg = new Configuration().configure();

            // Then connect to the db using the customized connection url
            cfg = cfg.setProperty("hibernate.connection.url", connUrl);
            //.setProperty("hibernate.connection.datasource", "java:comp/env/jdbc/test")
            sessionFactory = cfg.buildSessionFactory();

            R.log("HibernateUtil configuration and session factory built");

        } catch (HibernateException ex) {
            R.log("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
