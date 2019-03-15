package com.membership;

import com.membership.model.Member;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static Member member;

    @BeforeClass
    public static void setUp() {
        member = new Member();
        member.setFirstName("Edem");
        member.setLastName("Morny");
        member.setDateOfBirth(new Date());
    }

    @Test
    public void verifyNameAndDateOfBirth() {
        assertNotNull(member.getFirstName());
        assertNotNull(member.getLastName());
        assertNotNull(member.getDateOfBirth());
    }

    @Test
    public void verifyCreateMember() throws Exception {
        String json = "{\"firstName\":\"Baba\", \"lastName\":\"Captha\", \"dateOfBirth\":\"2012-12-04\"}";

        this.mockMvc.perform(post("/api/v1/member").contentType(MediaType.APPLICATION_JSON).content(json))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void verifyAddPlanToMember() throws Exception {
        String json = "{\"name\":\"Test Plan 2\", \"limited\":false}";
        MvcResult result = this.mockMvc.perform(post("/api/v1/plan")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .characterEncoding("utf-8"))
                                .andExpect(status().isCreated())
                                .andReturn();

        json = "{\"firstName\":\"Mama\", \"lastName\":\"High\", \"dateOfBirth\":\"2010-11-13\"}";
        MvcResult result2 = this.mockMvc.perform(post("/api/v1/member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .characterEncoding("utf-8"))
                                .andExpect(status().isCreated())
                                .andReturn();

        Long planId = (Long) ((JSONObject) new JSONParser().parse(result.getResponse().getContentAsString())).get("id");
        Long memberId = (Long) ((JSONObject) new JSONParser().parse(result2.getResponse().getContentAsString())).get("id");

        this.mockMvc.perform(put("/api/v1/member/add/" + memberId + "/" + planId).contentType(MediaType.APPLICATION_JSON).content(json))
                    .andExpect(status().isAccepted());
    }
}
