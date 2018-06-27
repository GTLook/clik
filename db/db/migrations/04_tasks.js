const TABLE_NAME = 'tasks'

exports.up = function(knex, Promise) {
  return knex.schema.createTable(TABLE_NAME, table=>{
    table.increments()
    table.integer('user_id').references('users.id')
    table.integer('team_id').notNullable().references('teams.id')
    table.string('title').notNullable().defaultTo('')
    table.text('text').notNullable().defaultTo('')
    table.integer('urgency').notNullable().defaultTo(0)
    table.timestamps(true,true)
  })
}

exports.down = function(knex, Promise) {
  return knex.schema.dropTableIfExists(TABLE_NAME)
}
