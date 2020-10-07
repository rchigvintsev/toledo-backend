package org.briarheart.orchestra.data;

import org.briarheart.orchestra.model.TaskTagRelation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Roman Chigvintsev
 */
public interface TaskTagRelationRepository extends ReactiveCrudRepository<TaskTagRelation, Void> {
    @Query("SELECT * FROM tasks_tags WHERE task_id = :task_id")
    Flux<TaskTagRelation> findByTaskId(Long taskId);

    @Query("SELECT * FROM tasks_tags WHERE task_id = :task_id AND tag_id = :tag_id")
    Mono<TaskTagRelation> findByTaskIdAndTagId(Long taskId, Long tagId);

    @Query("INSERT INTO tasks_tags (task_id, tag_id) VALUES (:taskId, :tagId)")
    Mono<Void> create(Long taskId, Long tagId);

    @Query("DELETE FROM tasks_tags WHERE task_id = :taskId AND tag_id = :tagId")
    Mono<Void> delete(Long taskId, Long tagId);
}