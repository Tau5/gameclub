package uno.tau0.gameclub;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {
    record GroupDTO(
            Long id,
            String name
    ) {
        GroupDTO(Group group) {
            this(group.id, group.name);
        }
    }

    @Autowired
    private GroupRespository repository;

    @GetMapping
    @SuppressWarnings("ClassEscapesDefinedScope")
    public Iterable<GroupDTO> findAll() {
        return repository.findAll().stream().map(GroupDTO::new).toList();
    }

    private record NewGroup(
            String name
    ) { }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SuppressWarnings("ClassEscapesDefinedScope")
    public Group create(@RequestBody NewGroup group) {
        return repository.save(new Group(group.name));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<User>> getMembers(@PathVariable Long id) {
       Optional<Group> groupMaybe = repository.findById(id);
       return groupMaybe.map(g ->
               ResponseEntity.ok(g.members)
       ).orElseGet(() ->
               ResponseEntity.status(404).body(null)
       );
    }
}
