const TABLE_NAME = 'teams'

exports.up = function(knex, Promise) {
  return knex.schema
    .createTable(TABLE_NAME, function(table){
      table.increments()
      table.string('team').notNullable().defaultTo('')
  })
};

exports.down = function(knex, Promise) {
  return knex.schema.dropTableIfExists(TABLE_NAME)
};
