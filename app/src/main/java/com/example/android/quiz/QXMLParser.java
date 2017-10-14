package com.example.android.quiz;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

class QXMLParser {

/*
 * There are 2 types XML files:
 * One contains quizes headers
 * Others XML files contain each quiz contents
 */
    static ArrayList<Quiz> LoadFromQuizList(InputStream xmlFile) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();
//            System.out.println("Корневой элемент: " + document.getDocumentElement().getNodeName());
            // получаем узлы с именем Language
            // теперь XML полностью загружен в память
            // в виде объекта Document
            NodeList quizesList = document.getElementsByTagName("quiz");

            // создадим из него список объектов Question
            ArrayList<Quiz> quizes=new ArrayList<>();
            quizes.clear();
            for (int i = 0; i < quizesList.getLength(); i++) {
                Node quiz = quizesList.item(i);
                if (quiz.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) quiz;

                    quizes.add(new Quiz(element.getAttribute("name"),element.getAttribute("lang"),element.getAttribute("file")));

                }

            }

            // печатаем в консоль информацию по каждому объекту Language
//            System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
//            for (String  quiz: quizes) {
//                System.out.println(quiz);
//            }
//            System.out.println("++++++++++++++++++++++++++++++++++++++++++++");

            return quizes;
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return null;

    }

/*
* Quiz Contents
 */
    static ArrayList<Question> LoadFromQuestions(InputStream xmlFile) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();
//            System.out.println("Корневой элемент: " + document.getDocumentElement().getNodeName());
            // получаем узлы с именем Language
            // теперь XML полностью загружен в память
            // в виде объекта Document
            NodeList questionList = document.getElementsByTagName("question");

            // создадим из него список объектов Question
            ArrayList<Question> questions=new ArrayList<>();
            questions.clear();
            for (int i = 0; i < questionList.getLength(); i++) {
                Node question = questionList.item(i);
                if (question.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) question;
//                    System.out.println(element.getTagName());
//                    System.out.println(element.getAttribute("text"));
//                    System.out.println(element.getAttribute("type"));


                    ArrayList<Answer> answers = new ArrayList<>();
                    NodeList answerList = questionList.item(i).getChildNodes();
                    for (int j = 0; j < answerList.getLength(); j++) {
                        Node answer = answerList.item(j);
                        if (answer.getNodeType() == Node.ELEMENT_NODE) {
//                            Element element2 = (Element) answer;
//                            System.out.println(element2.getTagName());

                            NodeList itemList = answer.getChildNodes();
                            for (int k = 0; k < itemList.getLength(); k++) {
                                Node item = itemList.item(k);
                                if (item.getNodeType() == Node.ELEMENT_NODE) {
                                    Element element1 = (Element) item;
//                                    System.out.println(element1.getTagName());
//                                    System.out.println(element1.getAttribute("id"));
//                                    System.out.println(element1.getAttribute("text"));
//                                    System.out.println(element1.getAttribute("correct"));
                                    answers.add(
                                            new Answer(Integer.valueOf(element1.getAttribute("id")),
                                                    element1.getAttribute("text"),
                                                    Boolean.parseBoolean(element1.getAttribute("correct"))
                                                    )
                                    );

                                }
                            }

                        }
                    }
                    questions.add(
                            new Question(
                                    element.getAttribute("text"),
                                    (element.getAttribute("type").equals("RadioGroup") ? 'R' : 'C'),
                                    answers,
                                    element.getAttribute("explanation"))
                    );

                }

            }

            // печатаем в консоль информацию по каждому объекту Language
//            System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
//            for (Question  question: english) {
//                System.out.println(question.getText());
//                System.out.println(question.getTypeControl());
//                for (Answer answer:question.getAnswerList()
//                        ) {
//                    System.out.println(answer.text);
//                    System.out.println(answer.rightAnswer);
//                }
//            }
            return questions;
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return null;

    }

}
