const TABLE_NAME = 'lanes'

exports.up = function(knex, Promise) {
  return knex.schema
    .createTable(TABLE_NAME, function(table){
      table.increments()
      table.string('lane').defaultTo('')
  })
};

exports.down = function(knex, Promise) {
  return knex.schema.dropTableIfExists(TABLE_NAME)
};
