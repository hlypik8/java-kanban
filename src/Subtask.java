public class Subtask extends Task {

    //Как я понял из ТЗ, сабтаски не могут существовать сами по себе, они обязательно должны
    //принадлежать какому-либо эпику. Эпик же в свою очередь может существовать и без сабтасков (то есть быть пустым)
    public Subtask(String name, String description) {
        super(name, description);
    }
}
