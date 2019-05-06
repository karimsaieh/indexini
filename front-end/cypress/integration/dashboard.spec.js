/* eslint-disable */


describe('test_dashboard', function() {
  it('search_total_is_there', function() {
    cy.viewport(1920, 665)
    cy.visit('http://localhost:9527/dashboard')
    cy.get('.el-row > .el-col-6:nth-child(1) > div > .el-card > .el-card__body').should('contain',"0")
  })
})
