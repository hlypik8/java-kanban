package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Epic extends Task {
    private final Collection<Subtask> subtaskList = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(int id, String name, String description) {
        super(id, name, description, Status.NEW, LocalDateTime.now(), Duration.ZERO);
        this.type = Type.EPIC;
        setEpicDuration();
        setEpicStartTime();
        setEndTime();
    }

    public Epic(Epic currentEpic) {
        super(currentEpic);
        this.subtaskList.addAll(currentEpic.getSubtaskList()); // Копируем подзадачи
        this.endTime = currentEpic.getEndTime();
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
        updateStatus(); //При добавлении новой подзадачи проверяем статус эпика
        setEpicDuration(); // устанавливаем длительность эпика (через расчет)
        setEpicStartTime(); // устанавливаем время начала эпика (также рассчитываем)
        setEndTime(); //Устанавливаем время конца эпика
    }

    public Collection<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void removeSubtask(Subtask subtask) {
        subtaskList.remove(subtask);
        updateStatus();
        setEpicDuration();
        setEpicStartTime();
        setEndTime();
    }

    public void updateStatus() {
        Set<Status> statuses = getSubtaskList().stream()
                .map(Subtask::getStatus)
                .collect(Collectors.toSet());

        this.status = statuses.size() == 1 ? statuses.iterator().next() : Status.IN_PROGRESS;
    }

    //Рассчитываем время начало эпика как время начала самого раннего сабтаска
    public Optional<LocalDateTime> calculateEpicStartTime() {
        if (subtaskList.isEmpty()) {
            return Optional.of(LocalDateTime.now());//Если сабтасков нет, то устанавливаем время создания эпика как
        } else {                                     //время начала эпика
            return subtaskList.stream()
                    .map(Subtask::getStartTime) // Получаем время начала сабтасков
                    .filter(Objects::nonNull)//Фильтруем от null значений (на всякий случай)
                    .min(Comparator.naturalOrder());//Возвращаем минимальный элемент стрима
        }
    }

    public void setEpicStartTime() {
        if (calculateEpicStartTime().isPresent()) {
            this.startTime = calculateEpicStartTime().get();
        }
    }

    //Рассчитываем длительность эпика как сумму длительностей всех сабтасков
    public Optional<Duration> calculateEpicDuration() {
        if (subtaskList.isEmpty()) {
            return Optional.of(Duration.ZERO); //Если список сабтасков пуст, то устанавливаем нулевую длительность
        } else {
            return subtaskList.stream()
                    .map(Subtask::getDuration) // Получаем время начала каждого сабтаска
                    .reduce(Duration::plus); // Суммируем
        }
    }

    public void setEpicDuration() {
        if (calculateEpicDuration().isPresent()) {
            this.duration = calculateEpicDuration().get();
        }
    }

    public Optional<LocalDateTime> calculateEpicEndTime() {
        if (subtaskList.isEmpty()) {
            return Optional.of(LocalDateTime.now());//Если сабтасков нет, то устанавливаем время создания эпика
        } else {                                     // как время окончания эпика
            return subtaskList.stream()
                    .map(Subtask::getEndTime) // Получаем время конца сабтасков
                    .filter(Objects::nonNull)//Фильтруем от null значений (на всякий случай)
                    .max(Comparator.naturalOrder());//Возвращаем максимальный элемент стрима
        }
    }

    public void setEndTime() {
        if (calculateEpicEndTime().isPresent()) {
            this.endTime = calculateEpicEndTime().get();
        }
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public void update(Task updatedTask) {
        super.update(updatedTask);
        updateStatus();//При обновлении эпика проверяем статус эпика по статусам подзадач
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                ", status=" + this.getStatus() +
                ", subtaskList=" + subtaskList +
                '}';
    }
}
