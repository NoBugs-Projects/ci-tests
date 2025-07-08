package com.example.teamcity.api;

import com.example.teamcity.api.requests.AgentAuthRequest;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.Test;

public class SetupTeamcityAgentTest extends BaseApiTest {
    private final String DEFAULT_NAME = "teamcityDefaultAgent";

    @Test(groups = {"Setup"})
    public void setupTeamCityAgentTest() {
        AgentAuthRequest agenAuthRequest = new AgentAuthRequest(Specifications.superUserSpec());
        agenAuthRequest.changeState(DEFAULT_NAME, true);
    }
}
