package com.membership;

import com.membership.model.Plan;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class PlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static Plan plan;

    @BeforeClass
    public static void setUp() {
        plan = new Plan();
        plan.setName("First Plan");
    }

    @Test
    public void verifyName() {
        assertNotNull(plan.getName());
    }

    @Test
    public void verifyLimitedDates() {
        plan.setStartDate(new Date());
        plan.setEndDate(new Date());
        plan.setLimited(true);

        assertTrue(plan.isLimited());
        assertNotNull(plan.getStartDate());
        assertNotNull(plan.getEndDate());
    }

    @Test
    public void verifyCreatePlan() throws Exception {
        String json = "{\"name\":\"Test Plan\", \"limited\":false}";

        this.mockMvc.perform(post("/api/v1/plan").contentType(MediaType.APPLICATION_JSON).content(json))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists());
    }
}
