/* eslint-disable */



describe('search_test', function() {
    
  beforeEach(function () {
    cy.server()
    cy.route({
      method: 'GET',
      url: /history\.*/,
      response: 'fixture:similarFiles.json'  
    })
    cy.route({
      method: 'GET',
      url: /Stats\.*/,
      response: 'fixture:similarFiles.json'  
    })
    cy.route({
      method: 'GET',
      url: /count\.*/,
      response: 'fixture:similarFiles.json'  
    })

    cy.route({
      method: 'GET',
      url: /means\.*/,
      response: 'fixture:similarFiles.json'  
    })
    cy.route({
        method: 'GET',
        url: '/search-ms/api/v1/search?query=taxe&size=10&page=0',
        response: 'fixture:search.json'  
    })

    cy.viewport(1920, 942)
  })

  it('top_bar_search_redirects_to_search_page', function() {

    cy.visit('http://localhost:9527/dashboard')
    cy.get('.app-wrapper > .main-container > .app-main > div > .fixed-width-page-parent-container').click()
    cy.get('div > .navbar > .right-menu > #header-search > .svg-icon').click()
    cy.get('.navbar > .right-menu > #header-search > .header-search-select > .el-input__inner').click()
    cy.get('.navbar > .right-menu > #header-search > .header-search-select > .el-input__inner').type('taxe{enter}')
    cy.get('.el-row > .el-col > .el-autocomplete > .el-input > .el-input__inner').should('have.value', 'taxe')
    cy.url().should('include', '/search/')
  })


  it('search_results_are_there', function() {

    cy.visit('http://localhost:9527/search/?query=taxe&size=10&page=1')
    cy.get('div:nth-child(1) > div > .el-card > .el-card__body > .show-tip:nth-child(6) > .fas').click()
    cy.get('div:nth-child(1) > div > .el-card > .el-card__body > .show-tip:nth-child(5) > .fas').click()
    cy.get('div:nth-child(1) > div > .el-card > .el-card__body > .show-tip > .el-icon-fa-caret-down').click()
    cy.get('.el-row > .el-col:nth-child(1) > div > div > .link-type').should('contain', 'DILA_Simulateur-calcul-interets-moratoires_Presentation_20170829.pdf')
    cy.get('div > .el-card > .el-card__body > .el-row > .tip').should('not.contain','%')    
  })

})
