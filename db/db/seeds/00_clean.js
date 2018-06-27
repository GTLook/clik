exports.seed = function(knex, Promise) {
  return knex('tasks').del()
  .then(function () {
    return knex('lanes').del()
  })
  .then(function () {
    return knex('teams').del()
  })
  .then(function () {
    return knex('users').del()
  })
}
