package org.example;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;
import java.util.Scanner;


public class Main {
    private static SessionFactory factory;
    int j = 0;
    public static void main(String[] args) {

        Configuration config = new Configuration();
        config.addAnnotatedClass(toDoList.class);
        config.configure("hibernate.cfg.xml");
        Main m = new Main();

        factory = config.buildSessionFactory();

        int i = 0;
        while(i != 4){
            System.out.println("To-Do List\n1. Add to-do item\n2. Delete from-do item\n3. Display all items\n4. Exit");
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            String name;

            switch(choice){
                case 1:
                    System.out.println("Enter the name of the to-do item");
                    name = sc.next();
                    m.addToDoList(name);
                    break;
                case 2:
                    System.out.println("Enter id # of to-do item to delete");
                    choice = sc.nextInt();
                    m.deleteToDoList(choice);
                    break;
                case 3:
                    m.displayAllToDoList();
                    break;
                case 4:
                    System.out.println("Thank you for using this program");
                    i = 4;
                    break;
                default:
                    System.out.println("Invalid choice");

            }
        }
    }

    public void addToDoList(String name){
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            toDoList list = new toDoList(name);
            session.persist(list);
            tx.commit();
        } catch (HibernateException e){
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    public void deleteToDoList(Integer id){
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            toDoList myList = session.find(toDoList.class, id);
            if(myList != null){
                session.remove(myList);
            }else{
                System.out.println("To-Do List item does not exist");
            }
            tx.commit();
        }catch (HibernateException e){
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    public void displayAllToDoList(){
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<toDoList> list = session.createQuery("from toDoList").list();
            for (toDoList item : list) {
                System.out.println(item);
            }
            tx.commit();
        }catch (HibernateException e){
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

}