package com.eorion.bo.enhancement.collaboration.adapter.inbound;

import org.springframework.beans.factory.annotation.Value;

import java.io.InputStreamReader;
import java.util.Objects;

public class BaseControllerTest {
    @Value("${spring.profiles.active:default}") // "default" if no profile is set
    private String activeProfile;

    protected InputStreamReader getFormDeleteInputStreamReader() {
        if (activeProfile.equals("testcontainers")) {
            return new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/form/delete-all.oracle.sql")));
        }
        return new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/form/delete-all.sql")));
    }

    protected InputStreamReader getAssociationDeleteInputStreamReader() {
        if (activeProfile.equals("testcontainers")) {
            return new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/bpez_cooperation_res_association/delete-all.oracle.sql")));
        }
        return new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/bpez_cooperation_res_association/delete-all.sql")));
    }

    protected InputStreamReader getConversationDeleteInputStreamReader() {
        if (activeProfile.equals("testcontainers")) {
            return new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/bpez_cooperation_node_conversation/delete-all.oracle.sql")));
        }
        return new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/bpez_cooperation_node_conversation/delete-all.sql")));
    }

    protected InputStreamReader getResourceDeleteInputStreamReader() {
        if (activeProfile.equals("testcontainers")) {
            return new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/resource/delete-all.oracle.sql")));
        }
        return new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/resource/delete-all.sql")));
    }

    protected InputStreamReader getResourceDetailDeleteStreamReader() {
        if (activeProfile.equals("testcontainers")) {
            return new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/bpez_cooperation_res_detail/delete-all.oracle.sql")));
        }
        return new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/bpez_cooperation_res_detail/delete-all.sql")));
    }

    protected InputStreamReader getMemberDeleteStreamReader() {
        if (activeProfile.equals("testcontainers")) {
            return new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/member/delete-all.oracle.sql")));
        }
        return new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/member/delete-all.sql")));
    }

    protected InputStreamReader getProjectDeleteStreamReader() {
        if (activeProfile.equals("testcontainers")) {
            return new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/project/delete-all.oracle.sql")));
        }
        return new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/project/delete-all.sql")));
    }
}
