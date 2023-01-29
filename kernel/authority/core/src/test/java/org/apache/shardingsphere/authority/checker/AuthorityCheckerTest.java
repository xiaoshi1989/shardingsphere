/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.authority.checker;

import org.apache.shardingsphere.authority.config.AuthorityRuleConfiguration;
import org.apache.shardingsphere.authority.rule.AuthorityRule;
import org.apache.shardingsphere.infra.binder.statement.ddl.CreateTableStatementContext;
import org.apache.shardingsphere.infra.binder.statement.dml.InsertStatementContext;
import org.apache.shardingsphere.infra.binder.statement.dml.SelectStatementContext;
import org.apache.shardingsphere.infra.config.algorithm.AlgorithmConfiguration;
import org.apache.shardingsphere.infra.executor.check.checker.SQLChecker;
import org.apache.shardingsphere.infra.metadata.database.rule.ShardingSphereRuleMetaData;
import org.apache.shardingsphere.infra.metadata.user.Grantee;
import org.apache.shardingsphere.infra.metadata.user.ShardingSphereUser;
import org.apache.shardingsphere.infra.util.spi.type.ordered.OrderedSPILoader;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Properties;

import static org.mockito.Mockito.mock;

public final class AuthorityCheckerTest {
    
    @SuppressWarnings("unchecked")
    @Test
    public void assertCheck() {
        Collection<ShardingSphereUser> users = new LinkedList<>();
        ShardingSphereUser root = new ShardingSphereUser("root", "", "localhost");
        users.add(root);
        AuthorityRuleConfiguration ruleConfig = new AuthorityRuleConfiguration(users, new AlgorithmConfiguration("ALL_PERMITTED", new Properties()));
        AuthorityRule rule = new AuthorityRule(ruleConfig, Collections.emptyMap());
        SQLChecker<AuthorityRule> sqlChecker = OrderedSPILoader.getServices(SQLChecker.class, Collections.singleton(rule)).get(rule);
        SelectStatementContext selectStatementContext = mock(SelectStatementContext.class);
        CreateTableStatementContext createTableStatementContext = mock(CreateTableStatementContext.class);
        InsertStatementContext insertStatementContext = mock(InsertStatementContext.class);
        sqlChecker.check(selectStatementContext, Collections.emptyList(), new Grantee("root", "localhost"), mock(ShardingSphereRuleMetaData.class), null, rule);
        sqlChecker.check(insertStatementContext, Collections.emptyList(), new Grantee("root", "localhost"), mock(ShardingSphereRuleMetaData.class), null, rule);
        sqlChecker.check(createTableStatementContext, Collections.emptyList(), new Grantee("root", "localhost"), mock(ShardingSphereRuleMetaData.class), null, rule);
    }
}
