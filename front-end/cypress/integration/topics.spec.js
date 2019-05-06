/* eslint-disable */



describe('test_topics', function() {
    
    beforeEach(function () {
      cy.server()
      cy.route({
          method: 'GET',
          url: 'http://localhost:3012/search-ms/api/v1/search/ldaTopics',
          response: 'fixture:topics.json'  
      })
      cy.viewport(1920, 942)
    })
  
    it('topics_are_there', function() {
      cy.visit('http://localhost:9527/topics/')
      cy.get('.el-table__body-wrapper > .el-table__body > tbody > .el-table__row:nth-child(1) > .el-table_1_column_2 > .cell > span:nth-child(3) > .el-button > span')
      .should('contain', 'zone')
    })

    it('go_to_search_when_clicking_on_a_topic', function() {
      cy.visit('http://localhost:9527/topics/')
      cy.get('.el-table__body-wrapper > .el-table__body > tbody > .el-table__row:nth-child(1) > .el-table_1_column_2 > .cell > span:nth-child(3) > .el-button > span').click()
      cy.url().should('include', '/search/')
    })
  })
  