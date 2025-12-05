package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    private static SessionFactory factory;

    public static void main(String[] args) {

        logger.info("Starting Hibernate To-Do CLI Application");

        Configuration config = new Configuration();
        config.addAnnotatedClass(toDoList.class);
        config.configure("hibernate.cfg.xml");

        factory = config.buildSessionFactory();

        Main m = new Main();
        Scanner sc = new Scanner(System.in);

        int i = 0;
        while (i != 4) {
            System.out.println("To-Do List\n1. Add to-do item\n2. Delete from-do item\n3. Display all items\n4. Exit");

            int choice = sc.nextInt();
            logger.info("User selected option {}", choice);

            String name;

            switch (choice) {
                case 1:
                    System.out.println("Enter the name of the to-do item:");
                    name = sc.next();
                    logger.debug("Adding item: {}", name);
                    m.addToDoList(name);
                    break;

                case 2:
                    System.out.println("Enter ID of item to delete:");
                    int idToDelete = sc.nextInt();
                    logger.debug("Deleting item with ID {}", idToDelete);
                    m.deleteToDoList(idToDelete);
                    break;

                case 3:
                    logger.info("Displaying all items");
                    m.displayAllToDoList();
                    break;

                case 4:
                    logger.info("Exiting application");
                    System.out.println("Thank you for using this program");
                    i = 4;
                    break;

                default:
                    logger.warn("Invalid menu choice: {}", choice);
                    System.out.println("Invalid choice");
            }
        }

        sc.close();
        factory.close();
        logger.info("Application closed");
    }

    public void addToDoList(String name) {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            toDoList list = new toDoList(name);
            session.persist(list);
            tx.commit();

            logger.info("Item added successfully: {}", name);

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            logger.error("Error adding item: {}", name, e);

        } finally {
            session.close();
        }
    }

    public void deleteToDoList(Integer id) {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            toDoList myList = session.find(toDoList.class, id);

            if (myList != null) {
                session.remove(myList);
                logger.info("Item deleted with ID {}", id);
            } else {
                logger.warn("Attempted to delete non-existent item ID {}", id);
                System.out.println("To-Do List item does not exist");
            }

            tx.commit();

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            logger.error("Error deleting item with ID {}", id, e);

        } finally {
            session.close();
        }
    }

    public void displayAllToDoList() {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List<toDoList> list = session.createQuery("from toDoList").list();

            logger.info("Fetched {} items from database", list.size());

            for (toDoList item : list) {
                System.out.println(item);
            }

            tx.commit();

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            logger.error("Error retrieving list", e);

        } finally {
            session.close();
        }
    }
}
