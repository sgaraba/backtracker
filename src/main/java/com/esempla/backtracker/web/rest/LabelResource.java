package com.esempla.backtracker.web.rest;

import com.esempla.backtracker.domain.Label;
import com.esempla.backtracker.repository.LabelRepository;
import com.esempla.backtracker.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.esempla.backtracker.domain.Label}.
 */
@RestController
@RequestMapping("/api/labels")
@Transactional
public class LabelResource {

    private static final Logger LOG = LoggerFactory.getLogger(LabelResource.class);

    private static final String ENTITY_NAME = "label";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LabelRepository labelRepository;

    public LabelResource(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    /**
     * {@code POST  /labels} : Create a new label.
     *
     * @param label the label to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new label, or with status {@code 400 (Bad Request)} if the label has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Label> createLabel(@Valid @RequestBody Label label) throws URISyntaxException {
        LOG.debug("REST request to save Label : {}", label);
        if (label.getId() != null) {
            throw new BadRequestAlertException("A new label cannot already have an ID", ENTITY_NAME, "idexists");
        }
        label = labelRepository.save(label);
        return ResponseEntity.created(new URI("/api/labels/" + label.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, label.getId().toString()))
            .body(label);
    }

    /**
     * {@code PUT  /labels/:id} : Updates an existing label.
     *
     * @param id the id of the label to save.
     * @param label the label to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated label,
     * or with status {@code 400 (Bad Request)} if the label is not valid,
     * or with status {@code 500 (Internal Server Error)} if the label couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Label> updateLabel(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Label label)
        throws URISyntaxException {
        LOG.debug("REST request to update Label : {}, {}", id, label);
        if (label.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, label.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!labelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        label = labelRepository.save(label);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, label.getId().toString()))
            .body(label);
    }

    /**
     * {@code GET  /labels} : get all the labels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of labels in body.
     */
    @GetMapping("")
    public List<Label> getAllLabels() {
        LOG.debug("REST request to get all Labels");
        return labelRepository.findAll();
    }

    /**
     * {@code GET  /labels/:id} : get the "id" label.
     *
     * @param id the id of the label to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the label, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Label> getLabel(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Label : {}", id);
        Optional<Label> label = labelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(label);
    }

    /**
     * {@code DELETE  /labels/:id} : delete the "id" label.
     *
     * @param id the id of the label to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Label : {}", id);
        labelRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
