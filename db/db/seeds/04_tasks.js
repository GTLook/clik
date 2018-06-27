const TABLE_NAME = 'tasks'

exports.seed = function(knex, Promise) {
  return knex(TABLE_NAME).del()
    .then(function () {
      return knex(TABLE_NAME).insert([
        {id: 1, user_id: 1, team_id: 1, title: "Can you take of this?", text: "Relevent desc", urgency: 1},
        {id: 2, user_id: 2, team_id: 1, title: "Do this.", text: "Urgent desc", urgency: 15},
        {id: 3, user_id: 1, team_id: 1, title: "Urgent!", text: "Panicked desc", urgency: 32},
        {id: 4, user_id: 3, team_id: 2, title: "BUG FIX", text: "Terrified desc", urgency: 54},
        {id: 5, user_id: 1, team_id: 2, title: "OMG the server is going to melt", text: "Meltdown desc", urgency: 100}
      ])
    })
    .then(() => {
      return knex.raw(`SELECT setval('${TABLE_NAME}_id_seq', (SELECT MAX(id) FROM ${TABLE_NAME}));`)
    })
}
