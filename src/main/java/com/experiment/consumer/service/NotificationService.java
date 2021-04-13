package com.experiment.consumer.service;

import com.experiment.consumer.domain.Notification;
import com.experiment.consumer.repository.NotificationRepository;
import com.experiment.consumer.security.SecurityUtils;
import com.experiment.consumer.service.dto.NotificationDTO;
import com.experiment.consumer.service.mapper.NotificationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Notification}.
 */
@Service
@Transactional
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationDTO save(NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    /**
     * Partially update a notification.
     *
     * @param notificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NotificationDTO> partialUpdate(NotificationDTO notificationDTO) {
        log.debug("Request to partially update Notification : {}", notificationDTO);

        return notificationRepository
            .findById(notificationDTO.getId())
            .map(
                existingNotification -> {
                    notificationMapper.partialUpdate(existingNotification, notificationDTO);
                    return existingNotification;
                }
            )
            .map(notificationRepository::save)
            .map(notificationMapper::toDto);
    }

    /**
     * Get all the notifications.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<NotificationDTO> findAll() {
        log.debug("Request to get all Notifications");
        return notificationRepository
            .findAll()
            .stream()
            .filter(x -> !x.getAcknowledged())
            .filter(x -> x.getUser().equalsIgnoreCase(SecurityUtils.getCurrentUserLogin().get()))
            .map(notificationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one notification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificationDTO> findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id).map(notificationMapper::toDto);
    }

    /**
     * Delete the notification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }
}
