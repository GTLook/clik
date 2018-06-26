const TABLE_NAME = 'lanes'

exports.seed = function(knex, Promise) {
  return knex(TABLE_NAME).del()
    .then(function () {
      return knex(TABLE_NAME).insert([
        {id: 1, lane: 'To Do'},
        {id: 2, lane: 'Doing'},
        {id: 3, lane: 'Done'},
        {id: 4, lane: 'Icebox'},
      ])
    })
    .then(() => {
      return knex.raw(`SELECT setval('${TABLE_NAME}_id_seq', (SELECT MAX(id) FROM ${TABLE_NAME}));`)
    })
}
