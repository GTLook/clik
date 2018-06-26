const TABLE_NAME = 'teams'

exports.seed = function(knex, Promise) {
  return knex(TABLE_NAME).del()
    .then(function () {
      return knex(TABLE_NAME).insert([
        {id: 1, team: "pipes"},
        {id: 2, team: "stergeon"},
        {id: 3, team: "stitch"},
        {id: 4, team: "galvanize"},
      ])
    })
    .then(() => {
      return knex.raw(`SELECT setval('${TABLE_NAME}_id_seq', (SELECT MAX(id) FROM ${TABLE_NAME}));`)
    })
}
