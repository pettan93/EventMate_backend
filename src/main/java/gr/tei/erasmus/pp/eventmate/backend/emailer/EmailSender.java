package gr.tei.erasmus.pp.eventmate.backend.emailer;

import gr.tei.erasmus.pp.eventmate.backend.models.EmailNotification;
import gr.tei.erasmus.pp.eventmate.backend.repository.EmailNotificationRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

@Component
public class EmailSender {


    private final EmailNotificationRepository emailNotificationRepository;

    private Queue<EmailNotification> emailNotifications = new Queue<EmailNotification>() {
        @Override
        public boolean add(EmailNotification emailNotification) {
            return false;
        }

        @Override
        public boolean offer(EmailNotification emailNotification) {
            return false;
        }

        @Override
        public EmailNotification remove() {
            return null;
        }

        @Override
        public EmailNotification poll() {
            return null;
        }

        @Override
        public EmailNotification element() {
            return null;
        }

        @Override
        public EmailNotification peek() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<EmailNotification> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends EmailNotification> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }
    }


    public EmailSender(EmailNotificationRepository emailNotificationRepository) {
        this.emailNotificationRepository = emailNotificationRepository;
    }




}
