const TABLE_NAME = 'users'

exports.up = function(knex, Promise) {
  return knex.schema
    .createTable(TABLE_NAME, (table) => {
      table.increments()
      table.string('user').notNullable().defaultTo('')
    })
}

exports.down = function(knex, Promise) {
  return knex.schema.dropTableIfExists(TABLE_NAME)
};
